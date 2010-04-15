/*
 * file:       ExtendedAttributeResourceFields.java
 * author:     Jon Iles
 * copyright:  (c) Packwood Software 2005
 * date:       02-Mar-2006
 */

/*
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 */

package net.sf.mpxj;

/**
 * This class is simply a container for a list of extended attribute fields.
 */

public final class ExtendedAttributeResourceFields
{
   /**
    * Constructor.
    */
   private ExtendedAttributeResourceFields()
   {
      // private constructor to prevent instantiation
   }

   /**
    * Array of extended attribute fields.
    */
   public static final ResourceField[] FIELD_ARRAY =
   {
      ResourceField.TEXT1,
      ResourceField.TEXT2,
      ResourceField.TEXT3,
      ResourceField.TEXT4,
      ResourceField.TEXT5,
      ResourceField.TEXT6,
      ResourceField.TEXT7,
      ResourceField.TEXT8,
      ResourceField.TEXT9,
      ResourceField.TEXT10,
      ResourceField.TEXT11,
      ResourceField.TEXT12,
      ResourceField.TEXT13,
      ResourceField.TEXT14,
      ResourceField.TEXT15,
      ResourceField.TEXT16,
      ResourceField.TEXT17,
      ResourceField.TEXT18,
      ResourceField.TEXT19,
      ResourceField.TEXT20,
      ResourceField.TEXT21,
      ResourceField.TEXT22,
      ResourceField.TEXT23,
      ResourceField.TEXT24,
      ResourceField.TEXT25,
      ResourceField.TEXT26,
      ResourceField.TEXT27,
      ResourceField.TEXT28,
      ResourceField.TEXT29,
      ResourceField.TEXT30,
      ResourceField.START1,
      ResourceField.START2,
      ResourceField.START3,
      ResourceField.START4,
      ResourceField.START5,
      ResourceField.START6,
      ResourceField.START7,
      ResourceField.START8,
      ResourceField.START9,
      ResourceField.START10,
      ResourceField.FINISH1,
      ResourceField.FINISH2,
      ResourceField.FINISH3,
      ResourceField.FINISH4,
      ResourceField.FINISH5,
      ResourceField.FINISH6,
      ResourceField.FINISH7,
      ResourceField.FINISH8,
      ResourceField.FINISH9,
      ResourceField.FINISH10,
      ResourceField.COST1,
      ResourceField.COST2,
      ResourceField.COST3,
      ResourceField.COST4,
      ResourceField.COST5,
      ResourceField.COST6,
      ResourceField.COST7,
      ResourceField.COST8,
      ResourceField.COST9,
      ResourceField.COST10,
      ResourceField.DATE1,
      ResourceField.DATE2,
      ResourceField.DATE3,
      ResourceField.DATE4,
      ResourceField.DATE5,
      ResourceField.DATE6,
      ResourceField.DATE7,
      ResourceField.DATE8,
      ResourceField.DATE9,
      ResourceField.DATE10,
      ResourceField.FLAG1,
      ResourceField.FLAG2,
      ResourceField.FLAG3,
      ResourceField.FLAG4,
      ResourceField.FLAG5,
      ResourceField.FLAG6,
      ResourceField.FLAG7,
      ResourceField.FLAG8,
      ResourceField.FLAG9,
      ResourceField.FLAG10,
      ResourceField.FLAG11,
      ResourceField.FLAG12,
      ResourceField.FLAG13,
      ResourceField.FLAG14,
      ResourceField.FLAG15,
      ResourceField.FLAG16,
      ResourceField.FLAG17,
      ResourceField.FLAG18,
      ResourceField.FLAG19,
      ResourceField.FLAG20,
      ResourceField.NUMBER1,
      ResourceField.NUMBER2,
      ResourceField.NUMBER3,
      ResourceField.NUMBER4,
      ResourceField.NUMBER5,
      ResourceField.NUMBER6,
      ResourceField.NUMBER7,
      ResourceField.NUMBER8,
      ResourceField.NUMBER9,
      ResourceField.NUMBER10,
      ResourceField.NUMBER11,
      ResourceField.NUMBER12,
      ResourceField.NUMBER13,
      ResourceField.NUMBER14,
      ResourceField.NUMBER15,
      ResourceField.NUMBER16,
      ResourceField.NUMBER17,
      ResourceField.NUMBER18,
      ResourceField.NUMBER19,
      ResourceField.NUMBER20,
      ResourceField.DURATION1,
      ResourceField.DURATION2,
      ResourceField.DURATION3,
      ResourceField.DURATION4,
      ResourceField.DURATION5,
      ResourceField.DURATION6,
      ResourceField.DURATION7,
      ResourceField.DURATION8,
      ResourceField.DURATION9,
      ResourceField.DURATION10,
      ResourceField.OUTLINE_CODE1,
      ResourceField.OUTLINE_CODE2,
      ResourceField.OUTLINE_CODE3,
      ResourceField.OUTLINE_CODE4,
      ResourceField.OUTLINE_CODE5,
      ResourceField.OUTLINE_CODE6,
      ResourceField.OUTLINE_CODE7,
      ResourceField.OUTLINE_CODE8,
      ResourceField.OUTLINE_CODE9,
      ResourceField.OUTLINE_CODE10,
      ResourceField.ENTERPRISE_COST1,
      ResourceField.ENTERPRISE_COST2,
      ResourceField.ENTERPRISE_COST3,
      ResourceField.ENTERPRISE_COST4,
      ResourceField.ENTERPRISE_COST5,
      ResourceField.ENTERPRISE_COST6,
      ResourceField.ENTERPRISE_COST7,
      ResourceField.ENTERPRISE_COST8,
      ResourceField.ENTERPRISE_COST9,
      ResourceField.ENTERPRISE_COST10,
      ResourceField.ENTERPRISE_DATE1,
      ResourceField.ENTERPRISE_DATE2,
      ResourceField.ENTERPRISE_DATE3,
      ResourceField.ENTERPRISE_DATE4,
      ResourceField.ENTERPRISE_DATE5,
      ResourceField.ENTERPRISE_DATE6,
      ResourceField.ENTERPRISE_DATE7,
      ResourceField.ENTERPRISE_DATE8,
      ResourceField.ENTERPRISE_DATE9,
      ResourceField.ENTERPRISE_DATE10,
      ResourceField.ENTERPRISE_DATE11,
      ResourceField.ENTERPRISE_DATE12,
      ResourceField.ENTERPRISE_DATE13,
      ResourceField.ENTERPRISE_DATE14,
      ResourceField.ENTERPRISE_DATE15,
      ResourceField.ENTERPRISE_DATE16,
      ResourceField.ENTERPRISE_DATE17,
      ResourceField.ENTERPRISE_DATE18,
      ResourceField.ENTERPRISE_DATE19,
      ResourceField.ENTERPRISE_DATE20,
      ResourceField.ENTERPRISE_DATE21,
      ResourceField.ENTERPRISE_DATE22,
      ResourceField.ENTERPRISE_DATE23,
      ResourceField.ENTERPRISE_DATE24,
      ResourceField.ENTERPRISE_DATE25,
      ResourceField.ENTERPRISE_DATE26,
      ResourceField.ENTERPRISE_DATE27,
      ResourceField.ENTERPRISE_DATE28,
      ResourceField.ENTERPRISE_DATE29,
      ResourceField.ENTERPRISE_DATE30,
      ResourceField.ENTERPRISE_DURATION1,
      ResourceField.ENTERPRISE_DURATION2,
      ResourceField.ENTERPRISE_DURATION3,
      ResourceField.ENTERPRISE_DURATION4,
      ResourceField.ENTERPRISE_DURATION5,
      ResourceField.ENTERPRISE_DURATION6,
      ResourceField.ENTERPRISE_DURATION7,
      ResourceField.ENTERPRISE_DURATION8,
      ResourceField.ENTERPRISE_DURATION9,
      ResourceField.ENTERPRISE_DURATION10,
      ResourceField.ENTERPRISE_FLAG1,
      ResourceField.ENTERPRISE_FLAG2,
      ResourceField.ENTERPRISE_FLAG3,
      ResourceField.ENTERPRISE_FLAG4,
      ResourceField.ENTERPRISE_FLAG5,
      ResourceField.ENTERPRISE_FLAG6,
      ResourceField.ENTERPRISE_FLAG7,
      ResourceField.ENTERPRISE_FLAG8,
      ResourceField.ENTERPRISE_FLAG9,
      ResourceField.ENTERPRISE_FLAG10,
      ResourceField.ENTERPRISE_FLAG11,
      ResourceField.ENTERPRISE_FLAG12,
      ResourceField.ENTERPRISE_FLAG13,
      ResourceField.ENTERPRISE_FLAG14,
      ResourceField.ENTERPRISE_FLAG15,
      ResourceField.ENTERPRISE_FLAG16,
      ResourceField.ENTERPRISE_FLAG17,
      ResourceField.ENTERPRISE_FLAG18,
      ResourceField.ENTERPRISE_FLAG19,
      ResourceField.ENTERPRISE_FLAG20,
      ResourceField.ENTERPRISE_NUMBER1,
      ResourceField.ENTERPRISE_NUMBER2,
      ResourceField.ENTERPRISE_NUMBER3,
      ResourceField.ENTERPRISE_NUMBER4,
      ResourceField.ENTERPRISE_NUMBER5,
      ResourceField.ENTERPRISE_NUMBER6,
      ResourceField.ENTERPRISE_NUMBER7,
      ResourceField.ENTERPRISE_NUMBER8,
      ResourceField.ENTERPRISE_NUMBER9,
      ResourceField.ENTERPRISE_NUMBER10,
      ResourceField.ENTERPRISE_NUMBER11,
      ResourceField.ENTERPRISE_NUMBER12,
      ResourceField.ENTERPRISE_NUMBER13,
      ResourceField.ENTERPRISE_NUMBER14,
      ResourceField.ENTERPRISE_NUMBER15,
      ResourceField.ENTERPRISE_NUMBER16,
      ResourceField.ENTERPRISE_NUMBER17,
      ResourceField.ENTERPRISE_NUMBER18,
      ResourceField.ENTERPRISE_NUMBER19,
      ResourceField.ENTERPRISE_NUMBER20,
      ResourceField.ENTERPRISE_NUMBER21,
      ResourceField.ENTERPRISE_NUMBER22,
      ResourceField.ENTERPRISE_NUMBER23,
      ResourceField.ENTERPRISE_NUMBER24,
      ResourceField.ENTERPRISE_NUMBER25,
      ResourceField.ENTERPRISE_NUMBER26,
      ResourceField.ENTERPRISE_NUMBER27,
      ResourceField.ENTERPRISE_NUMBER28,
      ResourceField.ENTERPRISE_NUMBER29,
      ResourceField.ENTERPRISE_NUMBER30,
      ResourceField.ENTERPRISE_NUMBER31,
      ResourceField.ENTERPRISE_NUMBER32,
      ResourceField.ENTERPRISE_NUMBER33,
      ResourceField.ENTERPRISE_NUMBER34,
      ResourceField.ENTERPRISE_NUMBER35,
      ResourceField.ENTERPRISE_NUMBER36,
      ResourceField.ENTERPRISE_NUMBER37,
      ResourceField.ENTERPRISE_NUMBER38,
      ResourceField.ENTERPRISE_NUMBER39,
      ResourceField.ENTERPRISE_NUMBER40,
      ResourceField.ENTERPRISE_TEXT1,
      ResourceField.ENTERPRISE_TEXT2,
      ResourceField.ENTERPRISE_TEXT3,
      ResourceField.ENTERPRISE_TEXT4,
      ResourceField.ENTERPRISE_TEXT5,
      ResourceField.ENTERPRISE_TEXT6,
      ResourceField.ENTERPRISE_TEXT7,
      ResourceField.ENTERPRISE_TEXT8,
      ResourceField.ENTERPRISE_TEXT9,
      ResourceField.ENTERPRISE_TEXT10,
      ResourceField.ENTERPRISE_TEXT11,
      ResourceField.ENTERPRISE_TEXT12,
      ResourceField.ENTERPRISE_TEXT13,
      ResourceField.ENTERPRISE_TEXT14,
      ResourceField.ENTERPRISE_TEXT15,
      ResourceField.ENTERPRISE_TEXT16,
      ResourceField.ENTERPRISE_TEXT17,
      ResourceField.ENTERPRISE_TEXT18,
      ResourceField.ENTERPRISE_TEXT19,
      ResourceField.ENTERPRISE_TEXT20,
      ResourceField.ENTERPRISE_TEXT21,
      ResourceField.ENTERPRISE_TEXT22,
      ResourceField.ENTERPRISE_TEXT23,
      ResourceField.ENTERPRISE_TEXT24,
      ResourceField.ENTERPRISE_TEXT25,
      ResourceField.ENTERPRISE_TEXT26,
      ResourceField.ENTERPRISE_TEXT27,
      ResourceField.ENTERPRISE_TEXT28,
      ResourceField.ENTERPRISE_TEXT29,
      ResourceField.ENTERPRISE_TEXT30,
      ResourceField.ENTERPRISE_TEXT31,
      ResourceField.ENTERPRISE_TEXT32,
      ResourceField.ENTERPRISE_TEXT33,
      ResourceField.ENTERPRISE_TEXT34,
      ResourceField.ENTERPRISE_TEXT35,
      ResourceField.ENTERPRISE_TEXT36,
      ResourceField.ENTERPRISE_TEXT37,
      ResourceField.ENTERPRISE_TEXT38,
      ResourceField.ENTERPRISE_TEXT39,
      ResourceField.ENTERPRISE_TEXT40
   };
}